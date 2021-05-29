package com.pam.pam_redesign;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pam.pam_redesign.databinding.FragmentCompletedTasksBinding;
import com.pam.pam_redesign.databinding.FragmentIncomingTasksBinding;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CompletedTasksFragment extends Fragment {

    private FragmentCompletedTasksBinding binding;
    public TodoDBService dbService;
    public DateTimeFormatter stringDateFormat;
    private ArrayList<TodoTask> completedTasksList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentCompletedTasksBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        stringDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        completedTasksList = new ArrayList<>();
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TodoTaskAdapterWithDate<TodoTask> adapt = new TodoTaskAdapterWithDate<TodoTask>(
                this.binding.getRoot().getContext(),
                completedTasksList
        ) {
            @Override
            public void onClickToEdit(View v) {
                super.onClickToEdit(v);
                int position = (Integer) v.getTag();
                Object object = getItem(position);
                TodoTask dataModel = (TodoTask) object;
                Bundle bundle = new Bundle();
//                bundle.putInt("dbID", dataModel.getDbId());
//                NavHostFragment.findNavController(CompletedTasksFragment.this)
//                        .navigate(R.id.action_TodayTasksFragment_to_editTodoFragment, bundle);
            }
        };

        Cursor dbCursor = dbService.getDataByDone(1, "DESC");
        while (dbCursor.moveToNext()) {
            boolean isDone = (dbCursor.getInt(1) != 0);
            LocalDate dueDate = LocalDate.parse(
                    dbCursor.getString(dbCursor.getColumnIndex("due_date"))
            );
            completedTasksList.add(new TodoTask(
                            dbCursor.getInt(dbCursor.getColumnIndex("todoTask_id")),
                            isDone,
                            dueDate,
                            dbCursor.getString(dbCursor.getColumnIndex("description")),
                            dbCursor.getInt(dbCursor.getColumnIndex("repetition"))
                    )
            );
        }

        binding.completedTasks.setAdapter(adapt);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}