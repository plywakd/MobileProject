package com.pam.pam_redesign;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.pam.pam_redesign.databinding.FragmentIncomingTasksBinding;

import org.jetbrains.annotations.NotNull;


public class IncomingTasksFragment extends Fragment {

    private FragmentIncomingTasksBinding binding;
    public TodoDBService dbService;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentIncomingTasksBinding.inflate(inflater, container, false);
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