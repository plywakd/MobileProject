package com.pam.pam_redesign;

import java.time.LocalDate;

public class TodoTask {

    private int dbId;
    private boolean done;
    private LocalDate dueDate;
    private String description;
    private Integer repeatOptionInDays;

    public TodoTask(LocalDate dueDate, String description) {
        this.done = false;
        this.dueDate = dueDate;
        this.description = description;
    }

    public TodoTask(LocalDate dueDate, String description, Integer repetition) {
        this.done = false;
        this.dueDate = dueDate;
        this.description = description;
        this.repeatOptionInDays = repetition;
    }

    public TodoTask(boolean done, LocalDate dueDate, String description, Integer repetition) {
        this.done = done;
        this.dueDate = dueDate;
        this.description = description;
        this.repeatOptionInDays = repetition;
    }

    public TodoTask(int dbId, boolean done, LocalDate dueDate, String description, Integer repetition) {
        this.dbId = dbId;
        this.done = done;
        this.dueDate = dueDate;
        this.description = description;
        this.repeatOptionInDays = repetition;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRepetition() {
        return repeatOptionInDays;
    }

    public void setRepetition(Integer repetition) {
        this.repeatOptionInDays = repetition;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    @Override
    public String toString() {
        return "TodoTask{" +
                "done=" + done +
                ", dueDate=" + dueDate +
                ", description='" + description + '\'' +
                ", repetition='" + repeatOptionInDays + '\'' +
                '}';
    }
}
