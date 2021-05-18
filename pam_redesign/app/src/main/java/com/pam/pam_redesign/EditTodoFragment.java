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
    private String repeatOption;
    public Integer taskID;
    public DateTimeFormatter stringDateFormat;
    private LocalDate todoDueDate;
    public HashMap<String, Integer> repeatOptionsMapSelector = new HashMap<String, Integer>() {{
        put("Daily", 1);
        put("Weekly", 2);
        put("Monthly", 3);
        put("Quarterly", 4);
    }};
    public HashMap<String, Integer> repeatOptionsMap = new HashMap<String, Integer>() {{
        put("Daily", 1);
        put("Weekly", 7);
        put("Monthly", 30);
        put("Quarterly", 90);
    }};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditTodoBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        stringDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        taskID = this.getArguments().getInt("dbID");
        fetchTaskData(taskID);
        System.out.println(originalTask.toString());
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
            repeatOption = "";
            binding.inputRepetition.setSelection(0);
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

        binding.updateTodo.setOnClickListener(click -> {
            if (repeatOption.equals("Select")) {
                repeatOption = "";
            }
            dbService.updateData(
                    taskID,
                    0,
                    todoDueDate.toString(),
                    binding.inputDescription.getText().toString(),
                    repeatOption
            );
            editRepeatTask(
                    todoDueDate,
                    binding.inputDescription.getText().toString(),
                    repeatOption
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
            Integer repetitionOptionPosition = 0;
            if (!foundTask.getString(4).equals("")) {
                repeatOption = foundTask.getString(4);
                repetitionOptionPosition = repeatOptionsMapSelector.get(repeatOption);
                binding.isRepeatable.setChecked(true);
            } else {
                binding.inputRepetition.setVisibility(View.INVISIBLE);
            }
            binding.inputRepetition.setSelection(repetitionOptionPosition);
            boolean isDone = (foundTask.getInt(1) != 0);
            originalTask = new TodoTask(
                    foundTask.getInt(0),
                    isDone,
                    todoDueDate,
                    foundTask.getString(3),
                    foundTask.getString(4)
            );

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void editRepeatTask(LocalDate taskDate, String description, String repetition) {
        if (!repetition.equals("") && !originalTask.getRepetition().equals("")) {
            String nextRepeatDateForTask = originalTask.getDueDate().plusDays(
                    repeatOptionsMap.get(originalTask.getRepetition())
            ).format(stringDateFormat);
            Cursor foundTask = dbService.getDataByParams(
                    nextRepeatDateForTask,
                    originalTask.getDescription(),
                    originalTask.getRepetition()
            );
            String newDateForTask = taskDate.plusDays(
                    repeatOptionsMap.get(repetition)
            ).format(stringDateFormat);
            if (foundTask.moveToNext()) {
                dbService.updateData(
                        foundTask.getInt(0),
                        0,
                        newDateForTask,
                        description,
                        repetition
                );
            }
        } else if(!originalTask.getRepetition().equals("")){
            String nextRepeatDateForTask = originalTask.getDueDate().plusDays(
                    repeatOptionsMap.get(originalTask.getRepetition())
            ).format(stringDateFormat);
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