package data.types;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTasksList = new ArrayList<>();

    public ArrayList<Integer> getSubTasksList() {
        return subTasksList;
    }

    public void addToSubTasksList(int subID) {
        this.subTasksList.add(subID);
    }

    public void deleteSubTaskFromList(int subID) {
        this.subTasksList.remove(subID);
    }

    public static EpicTask toEpic (Task task) {
        EpicTask epic = new EpicTask();
        epic.setTaskName(task.getTaskName());
        epic.setDescription(task.getDescription());
        epic.setStatus("NEW");
        epic.setCreationDate();
        return epic;
    }
}
