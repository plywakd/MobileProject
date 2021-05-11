package com.pam.pam_redesign;

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
    public ArrayList<String> tasks;
    private ArrayAdapter<String> adapter;
    private LocalDate selectedDate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

//                String date = day + "/" + (month + 1) + "/" + year; // month 0-11
//                System.out.println(date);
//                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d/MM/yyyy");
//                selectedDate = LocalDate.parse(date, dateFormat);
                selectedDate = LocalDate.of(year,month+1,day);
//                TODO load tasks on date selected? or filter them here?
            }
        });

//        TODO test tasks -> read from memory/storage/db and filter them by selectedDate
        ArrayList<TodoTask> testTasks = new ArrayList<TodoTask>();
        testTasks.add(new TodoTask(LocalDate.now(), "New adapter"));
        testTasks.add(new TodoTask(LocalDate.now(), "New adapter", "weekly"));

        TodoTaskAdapter<TodoTask> adapt = new TodoTaskAdapter<TodoTask>(this.binding.getRoot().getContext(), testTasks);
        binding.tasksForDateView.setAdapter(adapt);
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