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
import android.widget.DatePicker;

import com.google.android.material.snackbar.Snackbar;
import com.pam.pam_redesign.databinding.FragmentAddTodoBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddTodoFragment extends Fragment {

    private FragmentAddTodoBinding binding;
    final Calendar myCalendar = Calendar.getInstance();
    public TodoDBService dbService;

    private Integer repeatOptionInDays;
    public DateTimeFormatter stringDateFormat;
    private LocalDate todoDueDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        stringDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        todoDueDate = LocalDate.now();
        binding.inputDate.setText(todoDueDate.format(stringDateFormat));
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
                setTaskDateFromCalendarView();
            }
        };

        binding.inputDate.setOnClickListener((click) -> {
            new DatePickerDialog(this.binding.getRoot().getContext(), date, myCalendar
                    .get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        binding.isRepeatable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.inputRepetition.setVisibility(View.VISIBLE);
            } else {
                binding.inputRepetition.setVisibility(View.INVISIBLE);
            }
            binding.inputRepetition.setText("");
        });

        binding.addNewTodo.setOnClickListener((click) -> {
            repeatOptionInDays = binding.inputRepetition.getText().toString().equals("")
                    ? 0 : Integer.parseInt(binding.inputRepetition.getText().toString());
            if (binding.inputDescription.getText().toString().equals("")) {
                displayMessage("Please input some description!");
            } else {
                dbService.addData(
                        0,
                        todoDueDate.toString(),
                        binding.inputDescription.getText().toString(),
                        repeatOptionInDays
                );
                NavHostFragment.findNavController(AddTodoFragment.this)
                        .navigate(R.id.action_addTodoFragment_to_TodayTasksFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTaskDateFromCalendarView() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        LocalDate choosenDate = LocalDate.parse(formatter.format(myCalendar.getTime()));
        if (LocalDate.now().compareTo(choosenDate) > 0) {
            displayMessage("Please choose today or future date");
        } else {
            binding.inputDate.setText(choosenDate.toString());
            todoDueDate = choosenDate;
        }
    }

    public void displayMessage(String snackbarMessage) {
        Snackbar.make(binding.getRoot().getRootView(), snackbarMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}