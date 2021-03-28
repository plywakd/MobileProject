package com.pam.project;

import java.time.LocalDate;

public class TodoTask {

    private String taskDescription;
    private boolean checked;
    private LocalDate dueDate;

    public TodoTask(String taskDescription) {
        this.taskDescription = taskDescription;
        this.checked = false;
    }

    public TodoTask(String taskDescription, boolean checked) {
        this.taskDescription = taskDescription;
        this.checked = checked;
    }

    public TodoTask(String taskDescription, LocalDate dueDate) {
        this.taskDescription = taskDescription;
        this.checked = false;
        this.dueDate = dueDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        checked = checked;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
