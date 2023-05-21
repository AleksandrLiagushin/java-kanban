package data.types;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTasksIDsList = new ArrayList<>();

    public EpicTask(String taskName, String description, TaskStatus status) {
        super(taskName, description, status);
    }

    public ArrayList<Integer> getSubTasksIDsList() {
        return subTasksIDsList;
    }

    public void addSubIDToSubTasksList(int subID) {
        this.subTasksIDsList.add(subID);
    }

}
