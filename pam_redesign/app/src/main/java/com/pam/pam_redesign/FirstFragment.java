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
        createRepeatTasks(todayTaskList);
        TodoTaskAdapter<TodoTask> adapt = new TodoTaskAdapter<TodoTask>(this.binding.getRoot().getContext(), todayTaskList);
//      FOR TEST -> TO BE REMOVED IF NOT NEEDED
        Cursor dbCursorAllData = dbService.getData();
        while (dbCursorAllData.moveToNext()) {
            System.out.println(dbCursorAllData.getString(0) + ","
                    + dbCursorAllData.getString(1) + ","
                    + dbCursorAllData.getString(2) + ","
                    + dbCursorAllData.getString(3) + ","
                    + dbCursorAllData.getString(4));
        }
        String formattedDate = LocalDate.now().format(format);
        Cursor dbCursor = dbService.getDataByDate(formattedDate);
        while (dbCursor.moveToNext()) {
            boolean isDone = (dbCursor.getInt(1) != 0);
            LocalDate dueDate = LocalDate.parse(dbCursor.getString(2), format);
//            TODO implement id in class for updates in other fragments
            todayTaskList.add(new TodoTask(dbCursor.getInt(0),isDone, dueDate, dbCursor.getString(3), dbCursor.getString(4)));
        }

        binding.todayTasks.setAdapter(adapt);

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createRepeatTasks(ArrayList<TodoTask> todayTasks){
//        TODO for each repeatable task in todays list, create one new instance if next one doesnt exist!
        todayTasks.stream().filter((task) -> !task.getRepetition().equals("")).forEach((task)->{
                System.out.println(task.toString());
        });
    }

}