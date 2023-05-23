package data.storage;

import data.types.EpicTask;

import java.util.HashMap;
import java.util.Set;

public class EpicTasksList {
    private HashMap<Integer, EpicTask> epicTasksList = new HashMap<>();

    public void addEpicTaskToList(int taskID, EpicTask task) {
        this.epicTasksList.put(taskID, task);
    }

    public EpicTask getTaskByID(int epicID) {
        for (Integer id : epicTasksList.keySet()) {
            if (id == epicID) {
                return epicTasksList.get(id);
            }
        }
        return null;
    }

    public void deleteTaskByID(int taskID) {
        epicTasksList.remove(taskID);
    }

    public void deleteAllTasks() {
        epicTasksList.clear();
    }

    public HashMap<Integer, EpicTask> getEpicTasksList() {
        return epicTasksList;
    }

    @Override
    public String toString() {
        Set<Integer> ids = epicTasksList.keySet();
        StringBuilder out = new StringBuilder("EpicTasks{");
        for (Integer id : ids) {
            EpicTask epic = getTaskByID(id);
            out.append("TaskID='").append(id).append("' Task{")
                    .append(epic).append("}")
                    .append("SubTasksIDs=")
                    .append(epic.getSubTasksIDsList()).append("\n");
        }
        return out.toString();
    }
}
