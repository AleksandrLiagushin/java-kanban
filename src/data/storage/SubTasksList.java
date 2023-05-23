package data.storage;

import data.types.SubTask;
import data.types.Task;

import java.util.HashMap;
import java.util.Set;

public class SubTasksList {
    private HashMap<Integer, SubTask> subTasksList = new HashMap<>();

    public void addSubTaskToList(int taskID, Task task) {
        this.subTasksList.put(taskID, (SubTask) task);
    }

    public SubTask getTaskByID(int taskID) {
        for (Integer subTaskID : subTasksList.keySet()) {
            if (subTaskID == taskID) {
                return subTasksList.get(subTaskID);
            }
        }
        return null;
    }

    public void deleteTaskByID(int taskID) {
        subTasksList.remove(taskID);
    }

    public void deleteAllTasks() {
        subTasksList.clear();
    }

    public HashMap<Integer, SubTask> getSubTasksList() {
        return subTasksList;
    }

    @Override
    public String toString() {
        Set<Integer> ids = subTasksList.keySet();
        StringBuilder out = new StringBuilder("SubTasks{");
        for (Integer id : ids) {
            SubTask sub = subTasksList.get(id);
            out.append("TaskID='").append(id).append("' Task{").append(sub)
                    .append("' Epic='").append(sub.getOwnedByEpic()).append("}\n\t\t ");
        }
        return out.toString();
    }
}
