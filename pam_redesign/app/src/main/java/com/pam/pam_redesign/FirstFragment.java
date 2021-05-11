package com.pam.pam_redesign;

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
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<TodoTask> testTasks = new ArrayList<TodoTask>();
        testTasks.add(new TodoTask(LocalDate.now(), "Today task"));
        testTasks.add(new TodoTask(LocalDate.now(), "Today task 2 ", "weekly"));

        TodoTaskAdapter<TodoTask> adapt = new TodoTaskAdapter<TodoTask>(this.binding.getRoot().getContext(), testTasks);
        binding.todayTasks.setAdapter(adapt);
        binding.todayTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                TodoTask clickedTask = testTasks.get((int)id);
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