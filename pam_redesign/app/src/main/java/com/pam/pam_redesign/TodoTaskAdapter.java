package com.pam.pam_redesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class TodoTaskAdapter<T> extends ArrayAdapter<TodoTask> implements View.OnClickListener{

    private final Context context;
    private ArrayList<TodoTask> tasksList;

    private static class ViewHolder {
        CheckBox taskIsDone;
        TextView taskDescription;
        TextView taskRepetition;
    }

    public TodoTaskAdapter(Context context, ArrayList<TodoTask> tasksList) {
        super(context, R.layout.todo_task_row, tasksList);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.tasksList = tasksList;
    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TodoTask task = getItem(position);
        ViewHolder viewHolder;

        final View rowView;

        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.todo_task_row, parent, false);
            viewHolder.taskIsDone = (CheckBox) view.findViewById(R.id.done);
            viewHolder.taskDescription = (TextView) view.findViewById(R.id.description);
            viewHolder.taskRepetition = (TextView) view.findViewById(R.id.repetition);

            rowView = view;

            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
            rowView = view;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.top_to_bottom_scroll : R.anim.bottom_to_up_scroll);
        rowView.startAnimation(animation);
        lastPosition = position;

        viewHolder.taskIsDone.setChecked(true);
        viewHolder.taskDescription.setText(task.getDescription());
        viewHolder.taskRepetition.setText(task.getRepetition());
        viewHolder.taskIsDone.setOnClickListener(this);
        viewHolder.taskIsDone.setTag(position);

        return rowView;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TodoTask dataModel=(TodoTask)object;

        System.out.println(v.getId());
    }
}
