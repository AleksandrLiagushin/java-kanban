package data.storage;

import data.types.Task;

import java.util.HashMap;
import java.util.Set;

public class TasksList {
    private HashMap<Integer, Task> tasksList = new HashMap<>();

    public void addNewTaskToList(Integer taskID, Task task) {
        tasksList.put(taskID, task);
    }

    public Task getTaskByID(int taskID) {
        for (Integer subTaskID : tasksList.keySet()) {
            if (subTaskID == taskID) {
                return tasksList.get(subTaskID);
            }
        }
        return null;
    }

    public void deleteTaskByID(int taskID) {
        tasksList.remove(taskID);
    }

    public void deleteAllTasks() {
        tasksList.clear();
    }

    public HashMap<Integer, Task> getTasksList() {
        return tasksList;
    }

    @Override
    public String toString() {
        Set<Integer> ids = tasksList.keySet();
        StringBuilder out = new StringBuilder("NewTasks{");
        for (Integer id : ids) {
            Task task = getTaskByID(id);
            out.append("TaskID='").append(id).append("' Task{").append(task).append("}\n");
        }
        return out.toString();
    }
}
