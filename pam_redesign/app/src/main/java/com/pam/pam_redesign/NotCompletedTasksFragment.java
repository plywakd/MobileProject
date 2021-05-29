package com.pam.pam_redesign;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.pam.pam_redesign.databinding.FragmentNotCompletedTasksBinding;

import org.jetbrains.annotations.NotNull;


public class NotCompletedTasksFragment extends Fragment {

    private FragmentNotCompletedTasksBinding binding;
    public TodoDBService dbService;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentNotCompletedTasksBinding.inflate(inflater, container, false);
        dbService = new TodoDBService(binding.getRoot().getContext());
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}