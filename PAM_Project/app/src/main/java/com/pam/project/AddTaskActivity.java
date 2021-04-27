package com.pam.project;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private Spinner optionSpinner;
    private String repeatOption;

    public EditText taskDescription;
    public EditText taskDueDate;
    public FloatingActionButton addNewTask;
    final Calendar myCalendar = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_layout);
        optionSpinner = (Spinner) findViewById(R.id.repeatSpinner);
        addNewTask = (FloatingActionButton) findViewById(R.id.addTodoTask);
        taskDescription = (EditText) findViewById(R.id.inputDescription);
        taskDueDate = (EditText) findViewById(R.id.inputDueDate);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };

        taskDueDate.setOnClickListener((click) -> {
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        addNewTask.setOnClickListener((click) -> {
//            TODO implement adding to device memory or db?
            TodoTask toAdd = new TodoTask(taskDescription.getText().toString(), LocalDate.now());
            System.out.println("New task: " + toAdd.toString());
        });

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDateLabel() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        LocalDate choosenDate = LocalDate.parse(formatter.format(myCalendar.getTime()));
        if (LocalDate.now().compareTo(choosenDate) > 0) {
//            TODO implement alert of some sort?
            System.out.println("Wrong date, pick today's date or future one");
        }
        taskDueDate.setText(choosenDate.toString());
    }
}
