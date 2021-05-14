package com.pam.pam_redesign;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    public DateTimeFormatter format;
    private String selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        selectedDate = LocalDate.now().format(format);
        tasks = new ArrayList<TodoTask>();
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                tasks = new ArrayList<TodoTask>();
                selectedDate = LocalDate.of(year, month + 1, day).format(format);
                Cursor dbCursor = dbService.getDataByDate(selectedDate);
                while (dbCursor.moveToNext()) {
                    boolean isDone = (dbCursor.getInt(1) != 0);
                    LocalDate dueDate = LocalDate.parse(dbCursor.getString(2), format);
                    tasks.add(new TodoTask(isDone, dueDate, dbCursor.getString(3), dbCursor.getString(4)));
                }
                adapter = new TodoTaskAdapter<TodoTask>(binding.getRoot().getContext(), tasks);
                binding.tasksForDateView.setAdapter(adapter);
            }
        });

        binding.tasksForDateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TODO not working here, adapter method to be implemented
//                TodoTask clickedTask = tasks.get((int)id);
//                System.out.println(position + ", " + id);
//                listItem.setDone(!listItem.isDone());
//                System.out.println(listItem.isDone());
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

}