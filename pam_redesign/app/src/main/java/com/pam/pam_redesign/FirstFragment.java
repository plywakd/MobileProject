package com.pam.pam_redesign;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.pam.pam_redesign.databinding.FragmentFirstBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    public TodoDBService dbService;
    public DateTimeFormatter format;
    ArrayList<TodoTask> todayTaskList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        todayTaskList = new ArrayList<TodoTask>();
        TodoTaskAdapter<TodoTask> adapt = new TodoTaskAdapter<TodoTask>(this.binding.getRoot().getContext(), todayTaskList);
//      FOR TEST -> TO BE REMOVED IF NOT NEEDED
//        Cursor dbCursorAllData = dbService.getData();
//        while (dbCursorAllData.moveToNext()) {
//            System.out.println(dbCursorAllData.getString(2));
//        }
        String formattedDate = LocalDate.now().format(format);
        Cursor dbCursor = dbService.getDataByDate(formattedDate);
        while (dbCursor.moveToNext()) {
            boolean isDone = (dbCursor.getInt(1) != 0);
            LocalDate dueDate = LocalDate.parse(dbCursor.getString(2), format);
            todayTaskList.add(new TodoTask(isDone, dueDate, dbCursor.getString(3), dbCursor.getString(4)));
        }

        binding.todayTasks.setAdapter(adapt);
        binding.todayTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                TodoTask clickedTask = todayTaskList.get((int) id);
                System.out.println(position + ", " + id);
//                listItem.setDone(!listItem.isDone());
//                System.out.println(listItem.isDone());
            }
        });

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}