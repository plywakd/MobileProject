package com.pam.pam_redesign;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pam.pam_redesign.databinding.FragmentSecondBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    public TodoDBService dbService;
    public ArrayList<TodoTask> tasks;
    private ArrayAdapter<TodoTask> adapter;
    public DateTimeFormatter stringDateFormat;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        stringDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        tasks = fetchTaskForDate(LocalDate.now().format(stringDateFormat));
        adapter = new TodoTaskAdapter<TodoTask>(binding.getRoot().getContext(), tasks) {
            @Override
            public void onClickToEdit(View v) {
                super.onClickToEdit(v);
                int position = (Integer) v.getTag();
                Object object = getItem(position);
                TodoTask dataModel = (TodoTask) object;
                Bundle bundle = new Bundle();
                bundle.putInt("dbID", dataModel.getDbId());
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_editTodoFragment, bundle);
            }
        };
        binding.tasksForDateView.setAdapter(adapter);
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String selectedDate = LocalDate.of(year, month + 1, day).format(stringDateFormat);
                tasks = fetchTaskForDate(selectedDate);
                adapter = new TodoTaskAdapter<TodoTask>(binding.getRoot().getContext(), tasks) {
                    @Override
                    public void onClickToEdit(View v) {
                        super.onClickToEdit(v);
                        int position = (Integer) v.getTag();
                        Object object = getItem(position);
                        TodoTask dataModel = (TodoTask) object;
                        Bundle bundle = new Bundle();
                        bundle.putInt("dbID", dataModel.getDbId());
                        NavHostFragment.findNavController(SecondFragment.this)
                                .navigate(R.id.action_SecondFragment_to_editTodoFragment, bundle);
                    }
                };
                binding.tasksForDateView.setAdapter(adapter);
            }
        });

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<TodoTask> fetchTaskForDate(String date) {
        ArrayList<TodoTask> fetchedTasks = new ArrayList<TodoTask>();
        Cursor dbCursor = dbService.getDataByDate(date);
        while (dbCursor.moveToNext()) {
            boolean isDone = (dbCursor.getInt(1) != 0);
            LocalDate dueDate = LocalDate.parse(dbCursor.getString(2), stringDateFormat);
            fetchedTasks.add(new TodoTask(dbCursor.getInt(0), isDone, dueDate, dbCursor.getString(3), dbCursor.getInt(4)));
        }
        return fetchedTasks;
    }

}