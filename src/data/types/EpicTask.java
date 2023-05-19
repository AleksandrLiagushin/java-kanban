package data.types;

import java.util.ArrayList;

public class EpicTask extends Task {
    private ArrayList<Integer> subTasksList = new ArrayList<>();

    public ArrayList<Integer> getSubTasksList() {
        return subTasksList;
    }

    public void addSubIDToSubTasksList(int subID) {
        this.subTasksList.add(subID);
    }

}
