package data.types;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTasksIDsList = new ArrayList<>();

    public EpicTask(String taskName, String description) {
        super(taskName, description);
    }

    public ArrayList<Integer> getSubTasksIDsList() {
        return subTasksIDsList;
    }

    public void removeSubIDFromIDsList(int subID) {
        if (!subTasksIDsList.isEmpty()) {
            for (int i = 0; i < subTasksIDsList.size(); i++) {
                if (subTasksIDsList.get(i) == subID) {
                    subTasksIDsList.remove(i);
                    return;
                }
            }
        }
    }

    public void addSubIDToSubTasksList(int subID) {
        subTasksIDsList.add(subID);
    }

}
