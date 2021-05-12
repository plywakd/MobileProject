package com.pam.pam_redesign;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.google.android.material.snackbar.Snackbar;
import com.pam.pam_redesign.databinding.FragmentAddTodoBinding;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class AddTodoFragment extends Fragment {

    private FragmentAddTodoBinding binding;
    final Calendar myCalendar = Calendar.getInstance();
    public TodoDBService dbService;

    private String repeatOption;
    private LocalDate todoDueDate;
    private String description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.inputRepetition.setVisibility(View.INVISIBLE);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//               TODO maybe move to method:
                String dateFormat = "yyyy-MM-dd";
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.GERMAN);
                LocalDate choosenDate = LocalDate.parse(formatter.format(myCalendar.getTime()));
                if (LocalDate.now().compareTo(choosenDate) > 0) {
                    Snackbar.make(binding.getRoot().getRootView(), "Please choose today or future date", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    binding.inputDate.setText(choosenDate.toString());
                    todoDueDate = choosenDate;
                }
            }
        };

        binding.inputDate.setOnClickListener((click) -> {
            new DatePickerDialog(this.binding.getRoot().getContext(), date, myCalendar
                    .get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        binding.isRepeatable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.inputRepetition.setVisibility(View.VISIBLE);
                } else {
                    binding.inputRepetition.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.inputRepetition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeatOption = binding.inputRepetition.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                TODO if needed
            }
        });

        binding.addNewTodo.setOnClickListener((click) -> {
            TodoTask taskToSave;
            if (repeatOption.equals("Select")) {
                taskToSave = new TodoTask(todoDueDate, binding.inputDescription.getText().toString());
                dbService.addData(0, todoDueDate.toString(), binding.inputDescription.getText().toString(), "");
            } else {
                taskToSave = new TodoTask(todoDueDate, repeatOption, binding.inputDescription.getText().toString());
                dbService.addData(0, todoDueDate.toString(), binding.inputDescription.getText().toString(), repeatOption);
            }
            System.out.println(taskToSave.toString());
            NavHostFragment.findNavController(AddTodoFragment.this)
                    .navigate(R.id.action_addTodoFragment_to_FirstFragment);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}