package com.example.listugas.ui.add_task;

public class TaskModel {
    private String TaskID, TaskName, TaskDeadline_date, TaskDeadline_Time, TaskDesc, TaskPriority, TaskCategory;
    private Boolean TaskCompletion;

    public TaskModel(){}

    public TaskModel(String taskID, String taskName, String taskDeadline_date, String taskDeadline_Time, String taskDesc, String taskPriority, String taskCategory, Boolean taskCompletion) {
        TaskID = taskID;
        TaskName = taskName;
        TaskDeadline_date = taskDeadline_date;
        TaskDeadline_Time = taskDeadline_Time;
        TaskDesc = taskDesc;
        TaskPriority = taskPriority;
        TaskCategory = taskCategory;
        TaskCompletion = taskCompletion;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getTaskDeadline_date() {
        return TaskDeadline_date;
    }

    public void setTaskDeadline_date(String taskDeadline_date) {
        TaskDeadline_date = taskDeadline_date;
    }

    public String getTaskDeadline_Time() {
        return TaskDeadline_Time;
    }

    public void setTaskDeadline_Time(String taskDeadline_Time) {
        TaskDeadline_Time = taskDeadline_Time;
    }

    public String getTaskDesc() {
        return TaskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        TaskDesc = taskDesc;
    }

    public String getTaskPriority() {
        return TaskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        TaskPriority = taskPriority;
    }

    public String getTaskCategory() {
        return TaskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        TaskCategory = taskCategory;
    }

    public Boolean getTaskCompletion() {
        return TaskCompletion;
    }

    public void setTaskCompletion(Boolean taskCompletion) {
        TaskCompletion = taskCompletion;
    }
}
