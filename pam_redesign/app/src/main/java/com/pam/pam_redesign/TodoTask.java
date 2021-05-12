package com.pam.pam_redesign;

import java.time.LocalDate;

public class TodoTask {

    private boolean done;
    private LocalDate dueDate;
    private String description;
    private String repetition;

    public TodoTask(LocalDate dueDate, String description) {
        this.done = false;
        this.dueDate = dueDate;
        this.description = description;
    }

    public TodoTask(LocalDate dueDate, String description, String repetition) {
        this.done = false;
        this.dueDate = dueDate;
        this.description = description;
        this.repetition = repetition;
    }

    public TodoTask(boolean done, LocalDate dueDate, String description, String repetition) {
        this.done = done;
        this.dueDate = dueDate;
        this.description = description;
        this.repetition = repetition;
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

    public String getRepetition() {
        return repetition;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    @Override
    public String toString() {
        return "TodoTask{" +
                "done=" + done +
                ", dueDate=" + dueDate +
                ", description='" + description + '\'' +
                ", repetition='" + repetition + '\'' +
                '}';
    }
}
