package ru.yandex.practicum.kanban.data.model;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task { //не представляется возможным выполнить переименование класса согласно комментария
                                        //EpicTask --> Task т.к. возникает конфликт имен
                                        //и название Класса должно отражать его суть
    private final List<Integer> subtasksIds = new ArrayList<>();

    public EpicTask(int id, String taskName, String description) {
        super(id, taskName, description);
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void removeSubIdFromIDs(int subID) {
        if (subtasksIds.isEmpty()) {
            return;
        }
        for (int i = 0; i < subtasksIds.size(); i++) {
            if (subtasksIds.get(i) == subID) {
                subtasksIds.remove(i);
                return;
            }
        }

    }

    public void addSubIDToSubTasksList(int subID) {
        subtasksIds.add(subID);
    }

    @Override
    public String toString() {
        return "EpicTask{" + "ID='" + getId() +
                "' Task{" + "Name='" + getTaskName() +
                "', Descr='" + getDescription() +
                "', Status='" + getStatus() +
                " SubTasksIDs=" +
                getSubtasksIds() + "}\n";
    }
}
