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
import androidx.navigation.fragment.NavHostFragment;

import com.pam.pam_redesign.databinding.FragmentTodayTasksBinding;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TodayTasksFragment extends Fragment {

    private FragmentTodayTasksBinding binding;
    public TodoDBService dbService;
    public DateTimeFormatter stringDateFormat;
    private ArrayList<TodoTask> todayTaskList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentTodayTasksBinding.inflate(inflater, container, false);
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
                NavHostFragment.findNavController(TodayTasksFragment.this)
                        .navigate(R.id.action_TodayTasksFragment_to_editTodoFragment, bundle);
            }
        };

        String formattedDate = LocalDate.now().format(stringDateFormat);
        Cursor dbCursor = dbService.getDataByDate(formattedDate);
        while (dbCursor.moveToNext()) {
            boolean isDone = (dbCursor.getInt(1) != 0);
            LocalDate dueDate = LocalDate.parse(
                    dbCursor.getString(dbCursor.getColumnIndex("due_date"))
            );
            todayTaskList.add(new TodoTask(
                            dbCursor.getInt(dbCursor.getColumnIndex("todoTask_id")),
                            isDone,
                            dueDate,
                            dbCursor.getString(dbCursor.getColumnIndex("description")),
                            dbCursor.getInt(dbCursor.getColumnIndex("repetition"))
                    )
            );
        }

        binding.todayTasks.setAdapter(adapt);
        createRepeatTasks(todayTaskList);

        binding.buttonFirst.setOnClickListener(click -> NavHostFragment.findNavController(TodayTasksFragment.this)
                .navigate(R.id.action_TodayTasksFragment_to_CalendarViewFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createRepeatTasks(ArrayList<TodoTask> todayTasks) {
        todayTasks.stream().filter((task) -> task.getRepetition() != 0).forEach((task) -> {
            String nextRepeatDateForTask = task.getDueDate().plusDays(task.getRepetition()).format(stringDateFormat);
            Cursor foundTask = dbService.getDataByParams(nextRepeatDateForTask, task.getDescription(), task.getRepetition());
            if (!foundTask.moveToNext()) {
                dbService.addData(0, nextRepeatDateForTask, task.getDescription(), task.getRepetition());
            }
        });
    }

}