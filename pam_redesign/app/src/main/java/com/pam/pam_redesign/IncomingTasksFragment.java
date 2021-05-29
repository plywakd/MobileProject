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


import com.google.android.material.snackbar.Snackbar;
import com.pam.pam_redesign.databinding.FragmentIncomingTasksBinding;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class IncomingTasksFragment extends Fragment {

    private FragmentIncomingTasksBinding binding;
    public TodoDBService dbService;
    public DateTimeFormatter stringDateFormat;
    private ArrayList<TodoTask> incomingTaskList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentIncomingTasksBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        stringDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        incomingTaskList = new ArrayList<>();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.searchIncomingTasks.setOnClickListener((click) -> {
            System.out.println(binding.inputIncomingDays.getText().toString());
            if (binding.inputIncomingDays.getText().toString().equals("")) {
                displayMessage("Please input days number for incoming tasks range");
            } else {
                incomingTaskList = new ArrayList<>();
                TodoTaskAdapterWithDate<TodoTask> adapt = new TodoTaskAdapterWithDate<TodoTask>(
                        this.binding.getRoot().getContext(),
                        incomingTaskList
                ) {
                    @Override
                    public void onClickToEdit(View v) {
                        super.onClickToEdit(v);
                        int position = (Integer) v.getTag();
                        Object object = getItem(position);
                        TodoTask dataModel = (TodoTask) object;
                        Bundle bundle = new Bundle();
                        bundle.putInt("dbID", dataModel.getDbId());
                        NavHostFragment.findNavController(IncomingTasksFragment.this)
                                .navigate(R.id.action_incomingTasksFragment_to_editTodoFragment, bundle);
                    }
                };

                LocalDate dateTo = LocalDate.now().plusDays(
                        Integer.parseInt(binding.inputIncomingDays.getText().toString())
                );
                Cursor dbCursor = dbService.getDataBetweenDateRange(
                        LocalDate.now().format(stringDateFormat),
                        dateTo.format(stringDateFormat)
                );
                while (dbCursor.moveToNext()) {
                    boolean isDone = (dbCursor.getInt(1) != 0);
                    LocalDate dueDate = LocalDate.parse(
                            dbCursor.getString(dbCursor.getColumnIndex("due_date"))
                    );
                    incomingTaskList.add(
                            new TodoTask(
                                    dbCursor.getInt(dbCursor.getColumnIndex("todoTask_id")),
                                    isDone,
                                    dueDate,
                                    dbCursor.getString(dbCursor.getColumnIndex("description")),
                                    dbCursor.getInt(dbCursor.getColumnIndex("repetition"))
                            )
                    );
                }
                binding.incomingTasks.setAdapter(adapt);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void displayMessage(String snackbarMessage) {
        Snackbar.make(binding.getRoot().getRootView(), snackbarMessage, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}