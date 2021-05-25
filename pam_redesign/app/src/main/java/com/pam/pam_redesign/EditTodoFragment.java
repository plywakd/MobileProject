package com.pam.pam_redesign;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.pam.pam_redesign.databinding.FragmentEditTodoBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditTodoFragment extends Fragment {

    private FragmentEditTodoBinding binding;
    final Calendar myCalendar = Calendar.getInstance();
    public TodoDBService dbService;

    private TodoTask originalTask;
    private Integer repeatOptionInDays;
    public Integer taskID;
    public DateTimeFormatter stringDateFormat;
    private LocalDate todoDueDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditTodoBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        stringDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        taskID = this.getArguments().getInt("dbID");
        fetchTaskData(taskID);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
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
            repeatOptionInDays = 0;
        });

        binding.updateTodo.setOnClickListener(click -> {
            repeatOptionInDays = binding.inputRepetition.getText().toString().equals("") ? 0 : Integer.parseInt(binding.inputRepetition.getText().toString());
            dbService.updateData(
                    taskID,
                    0,
                    todoDueDate.toString(),
                    binding.inputDescription.getText().toString(),
                    repeatOptionInDays
            );
            editRepeatTask(
                    todoDueDate,
                    binding.inputDescription.getText().toString(),
                    repeatOptionInDays
            );
            NavHostFragment.findNavController(EditTodoFragment.this)
                    .navigate(R.id.action_editTodoFragment_to_FirstFragment);
        });

        binding.deleteTodo.setOnClickListener(click -> {
            deleteTask(taskID, "Task successfuly deleted");
            NavHostFragment.findNavController(EditTodoFragment.this)
                    .navigate(R.id.action_editTodoFragment_to_FirstFragment);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetchTaskData(Integer taskId) {
        Cursor foundTask = dbService.getDataById(taskId);
        if (foundTask.moveToNext()) {
            todoDueDate = LocalDate.parse(foundTask.getString(2));
            binding.inputDate.setText(foundTask.getString(2));
            binding.inputDescription.setText(foundTask.getString(3));
            if (foundTask.getInt(4) != 0) {
                repeatOptionInDays = foundTask.getInt(4);
                binding.isRepeatable.setChecked(true);
                System.out.println(repeatOptionInDays);
                binding.inputRepetition.setText(repeatOptionInDays.toString());
            } else {
                binding.inputRepetition.setVisibility(View.INVISIBLE);
            }
            boolean isDone = (foundTask.getInt(1) != 0);
            originalTask = new TodoTask(
                    foundTask.getInt(0),
                    isDone,
                    todoDueDate,
                    foundTask.getString(3),
                    foundTask.getInt(4)
            );

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void editRepeatTask(LocalDate taskDate, String description, Integer repetition) {
        if (repetition != 0 && originalTask.getRepetition() != 0) {
            String nextRepeatDateForTask = originalTask.getDueDate()
                    .plusDays(originalTask.getRepetition())
                    .format(stringDateFormat);
            Cursor foundTask = dbService.getDataByParams(
                    nextRepeatDateForTask,
                    originalTask.getDescription(),
                    originalTask.getRepetition()
            );
            String newDateForTask = taskDate.plusDays(repetition).format(stringDateFormat);
            if (foundTask.moveToNext()) {
                dbService.updateData(
                        foundTask.getInt(0),
                        foundTask.getInt(1),
                        newDateForTask,
                        description,
                        repetition
                );
            }
        } else if (originalTask.getRepetition() != 0) {
            String nextRepeatDateForTask = originalTask.getDueDate()
                    .plusDays(originalTask.getRepetition())
                    .format(stringDateFormat);
            Cursor foundTask = dbService.getDataByParams(
                    nextRepeatDateForTask,
                    originalTask.getDescription(),
                    originalTask.getRepetition()
            );
            if (foundTask.moveToNext()) {
                deleteTask(foundTask.getInt(0), "deleted future instance of repetited task");
            }
        }
    }

    public void deleteTask(Integer taskToDeleteId, String snackbarMessage) {
        dbService.deleteById(taskToDeleteId);
        Snackbar.make(binding.getRoot().getRootView(), snackbarMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}