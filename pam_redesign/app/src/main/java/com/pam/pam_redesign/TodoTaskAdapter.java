package com.pam.pam_redesign;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class TodoTaskAdapter<T> extends ArrayAdapter<TodoTask> implements View.OnClickListener {

    private final Context context;
    private ArrayList<TodoTask> tasksList;
    public TodoDBService dbService;
    public DateTimeFormatter format;


    private static class ViewHolder {
        CheckBox taskIsDone;
        TextView taskDescription;
        TextView taskRepetition;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TodoTaskAdapter(Context context, ArrayList<TodoTask> tasksList) {
        super(context, R.layout.todo_task_row, tasksList);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.tasksList = tasksList;
        this.dbService = new TodoDBService(context);
        format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TodoTask task = getItem(position);
        ViewHolder viewHolder;

        final View rowView;

        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.todo_task_row, parent, false);
            viewHolder.taskIsDone = (CheckBox) view.findViewById(R.id.done);
            viewHolder.taskDescription = (TextView) view.findViewById(R.id.description);
            viewHolder.taskRepetition = (TextView) view.findViewById(R.id.repetition);

            rowView = view;

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            rowView = view;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.top_to_bottom_scroll : R.anim.bottom_to_up_scroll);
        rowView.startAnimation(animation);
        lastPosition = position;

        viewHolder.taskIsDone.setChecked(task.isDone());
        viewHolder.taskDescription.setText(task.getDescription());
        viewHolder.taskRepetition.setText(String.format("Every %d day/s", task.getRepetition()));
        viewHolder.taskIsDone.setOnClickListener(this);
        viewHolder.taskDescription.setOnClickListener(this::onClickToEdit);
        viewHolder.taskRepetition.setOnClickListener(this::onClickToEdit);
        viewHolder.taskIsDone.setTag(position);
        viewHolder.taskDescription.setTag(position);
        viewHolder.taskRepetition.setTag(position);

        return rowView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        TodoTask dataModel = (TodoTask) object;
        dbService.updateData(
                dataModel.getDbId(),
                !dataModel.isDone() ? 1 : 0,
                dataModel.getDueDate().format(format),
                dataModel.getDescription(),
                dataModel.getRepetition()
        );
    }

    public void onClickToEdit(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        TodoTask dataModel = (TodoTask) object;
    }
}
