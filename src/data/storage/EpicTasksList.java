package data.storage;

import data.types.EpicTask;

import java.util.ArrayList;
import java.util.Arrays;
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
        System.out.println("Эпик с таким ID не найден=(");
        return null;
    }

    public void deleteTaskByID(int taskID) {
        this.epicTasksList.remove(taskID);
    }

    public void deleteAllTasks() {
        this.epicTasksList.clear();
    }

    public void addSubTaskByID(int epicID, int subTaskID) {
        EpicTask epic;
        for (Integer ID : this.epicTasksList.keySet()) {
            if (ID == epicID) {
                epic = this.epicTasksList.get(ID);
                epic.addToSubTasksList(subTaskID);
                this.epicTasksList.put(epicID, epic);
                return;
            }
        }
    }

    public void changeEpicStatus(int epicID, SubTasksList subTasksList) {
        ArrayList<Integer> tasksByEpic = getTaskByID(epicID).getSubTasksList();
        EpicTask epic = getTaskByID(epicID);
        String status = "NEW";
        if (!tasksByEpic.isEmpty()) {
            status = subTasksList.getTaskByID(tasksByEpic.get(0)).getStatus();
            for (Integer integer : tasksByEpic) {
                if (!status.equals(subTasksList.getTaskByID(integer).getStatus())) {
                    status = "IN_PROGRESS";
                    break;
                }
            }
        }
        epic.setStatus(status);
        addEpicTaskToList(epicID, epic);
    }

    @Override
    public String toString() {
        Set<Integer> ids = this.epicTasksList.keySet();
        StringBuilder out = new StringBuilder("EpicTasks{");
        for (Integer id : ids) {
            EpicTask epic = getTaskByID(id);
            out.append("TaskID='").append(id).append("' Task{").append(Arrays.toString(new EpicTask[]{epic})).append("}\n");
        }
        return out.toString();
    }
}
