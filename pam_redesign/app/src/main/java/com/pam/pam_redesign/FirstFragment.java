package com.pam.pam_redesign;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.pam.pam_redesign.databinding.FragmentFirstBinding;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    public TodoDBService dbService;
    public DateTimeFormatter stringDateFormat;
    private ArrayList<TodoTask> todayTaskList;
    //    Map day -> 1 day, week -> 7 days, month -> 30 days, quarter -> 90 days
//    More option to be implemented
//    Maybe some dynamic range set support like: REPEAT EVERY [X] DAYS in view where X is input number
    public HashMap<String, Integer> repeatOptionsMap = new HashMap<String, Integer>() {{
        put("Daily", 1);
        put("Weekly", 7);
        put("Monthly", 30);
        put("Quarterly", 90);
    }};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        stringDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        todayTaskList = new ArrayList<>();
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TodoTaskAdapter<TodoTask> adapt = new TodoTaskAdapter<TodoTask>(this.binding.getRoot().getContext(), todayTaskList) {
            @Override
            public void onClickToEdit(View v) {
                super.onClickToEdit(v);
                int position = (Integer) v.getTag();
                Object object = getItem(position);
                TodoTask dataModel = (TodoTask) object;
                Bundle bundle = new Bundle();
                bundle.putInt("dbID", dataModel.getDbId());
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_editTodoFragment, bundle);
            }
        };
//      FOR TEST -> TO BE REMOVED IF NOT NEEDED
        Cursor dbCursorAllData = dbService.getData();
        while (dbCursorAllData.moveToNext()) {
            System.out.println(dbCursorAllData.getString(0) + ","
                    + dbCursorAllData.getString(1) + ","
                    + dbCursorAllData.getString(2) + ","
                    + dbCursorAllData.getString(3) + ","
                    + dbCursorAllData.getString(4));
        }
        String formattedDate = LocalDate.now().format(stringDateFormat);
        Cursor dbCursor = dbService.getDataByDate(formattedDate);
        while (dbCursor.moveToNext()) {
            boolean isDone = (dbCursor.getInt(1) != 0);
            LocalDate dueDate = LocalDate.parse(dbCursor.getString(2), stringDateFormat);
            todayTaskList.add(new TodoTask(dbCursor.getInt(0), isDone, dueDate, dbCursor.getString(3), dbCursor.getString(4)));
        }

        binding.todayTasks.setAdapter(adapt);
        createRepeatTasks(todayTaskList);

        binding.buttonFirst.setOnClickListener(click -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createRepeatTasks(ArrayList<TodoTask> todayTasks) {
        todayTasks.stream().filter((task) -> !task.getRepetition().equals("")).forEach((task) -> {
            String nextRepeatDateForTask = task.getDueDate().plusDays(repeatOptionsMap.get(task.getRepetition())).format(stringDateFormat);
            System.out.println(repeatOptionsMap.get(task.getRepetition()) + "," + nextRepeatDateForTask);
            Cursor foundTask = dbService.getDataByParams(nextRepeatDateForTask, task.getDescription(), task.getRepetition());
            if (!foundTask.moveToNext()) {
                dbService.addData(0, nextRepeatDateForTask, task.getDescription(), task.getRepetition());
            }
        });
    }

}