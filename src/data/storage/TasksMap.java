package data.storage;

import data.types.Task;

import java.util.HashMap;

public class TasksMap<T extends Task> {

    private final HashMap<Integer, T> tasksList = new HashMap<>();

    public void addTaskToMap(Integer taskID, T task) {
        tasksList.put(taskID, task);
    }

    public T getTaskByID(int taskID) {
        return tasksList.get(taskID);
    }

    public void deleteTaskByID(int taskID) {
        tasksList.remove(taskID);
    }

    public void deleteAllTasks() {
        tasksList.clear();
    }

    public HashMap<Integer, T> getTasksMap() {
        return tasksList;
    }

}
